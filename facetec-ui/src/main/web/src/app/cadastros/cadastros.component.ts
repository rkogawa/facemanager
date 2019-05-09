import { Component, ViewChild, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { FacetecService } from "../services/facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { Pessoa } from "./pessoa";
import { DeviceService } from "../services/device.service";

@Component({
    selector: 'app-cadastros',
    templateUrl: './cadastros.component.html',
    styleUrls: ['./cadastros.component.scss']
})
export class CadastrosComponent {

    form: FormGroup;

    backendPath: string = 'pessoa';

    edicao = false;

    informacoesAcesso: string[] = ['Permanente', 'Visitante'];

    public cpfMask = {
        guide: true,
        showMask: false,
        mask: [/\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/]
    };

    public timeMask = {
        guide: true,
        showMask: false,
        mask: [/\d/, /\d/, ':', /\d/, /\d/]
    };

    @ViewChild('file') file;
    public files: Set<File> = new Set();
    imageSrc: string;

    constructor(
        private fb: FormBuilder,
        private service: FacetecService,
        private deviceService: DeviceService,
        private feedbackService: FeedbackService
    ) {
        this.createForm(new Pessoa());
    }

    createForm(pessoa: Pessoa) {
        this.form = this.fb.group({
            nome: [pessoa.nome, Validators.required],
            informacaoAcesso: [pessoa.informacaoAcesso, Validators.required],
            grupo: pessoa.grupo,
            cpf: [pessoa.cpf, Validators.required],
            telefone: pessoa.telefone,
            celular: pessoa.celular,
            email: pessoa.email,
            dataInicio: pessoa.dataInicio,
            horaInicio: pessoa.horaInicio,
            dataFim: pessoa.dataFim,
            horaFim: pessoa.horaFim,
            dataUltimoAcesso: [{ value: pessoa.dataUltimoAcesso, disabled: true }],
            horaUltimoAcesso: [{ value: pessoa.horaUltimoAcesso, disabled: true }],
            comentario: pessoa.comentario,
            foto: [pessoa.foto, Validators.required],
            id: pessoa.id
        })
        this.edicao = false;
        this.imageSrc = null;
    }

    addFiles() {
        this.file.nativeElement.click();
    }

    onFilesAdded(event: Event): void {
        const target = event.target as any;
        if (target.files && target.files[0]) {
            const file = target.files[0];

            this.form.get('foto').setValue(file);
            const reader = new FileReader();
            reader.onload = e => this.imageSrc = reader.result as string;

            reader.readAsDataURL(file);
        }
    }

    save() {
        const formData: FormData = new FormData();
        const param = this.form.value;
        Object.keys(param).forEach(key => {
            if (param[key] instanceof File) {
                formData.append(key, param[key], param[key].name);
            } else {
                formData.append(key, param[key]);
            }
        });
        this.service.create(this.backendPath, formData).subscribe(
            success => {
                this.feedbackService.showSuccessMessage('Registro cadastrado com sucesso. Iniciando envio da pessoa para aparelhos...');
                this.deviceService.createPerson(this.form.value);
                this.createForm(new Pessoa());
            }
        );
    }

    pesquisar() {
        this.service.get<Pessoa>(`${this.backendPath}/${this.form.get('cpf').value}`).subscribe(
            result => {
                this.createForm(result);
                this.edicao = true;
                this.imageSrc = `data:image/jpeg;base64,${result.foto}`;
            },
            error => this.novo()
        )
    }

    novo() {
        this.createForm(new Pessoa());
    }

    excluir() {
        this.service.delete(this.backendPath, this.form.get('id').value).subscribe(
            result => {
                this.feedbackService.showSuccessMessage('Registro excluído com sucesso.');
                this.deviceService.deletePerson(this.form.get('cpf').value);
                this.novo();
            }
        )
    }

    isCpfPreenchido() {
        return this.form.get('cpf').value.length > 0;
    }

    isPessoaIdPreenchido() {
        return this.form.get('id').value !== null && this.form.get('id').value.length > 0;
    }

}
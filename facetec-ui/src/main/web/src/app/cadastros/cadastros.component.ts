import { Component, ViewChild, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { FacetecService } from "../services/facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { Pessoa, PessoaResponse } from "./pessoa";
import { AsyncButtonDirective } from "../services/async-button.directive";
import { finalize } from "rxjs/operators";
import { WebcamUtil, WebcamImage } from "ngx-webcam";
import { Subject, Observable } from "rxjs";
import { MatDialog } from "@angular/material";
import { FeedbackIntegracaoDialogComponent } from "./feedback-integracao-dialog.component";

@Component({
    selector: 'app-cadastros',
    templateUrl: './cadastros.component.html',
    styleUrls: ['./cadastros.component.scss']
})
export class CadastrosComponent {

    form: FormGroup;

    formFoto: FormGroup;

    backendPath: string = 'pessoa';

    edicao = false;
    cpfPesquisa = '';

    informacoesAcesso: string[] = ['Permanente', 'Visitante'];

    private webcamTrigger: Subject<void> = new Subject<void>();

    public webcamImage: WebcamImage = null;

    @ViewChild('btnPesquisar') btnPesquisar: AsyncButtonDirective;

    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;

    @ViewChild('btnExcluir') btnExcluir: AsyncButtonDirective;

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
        public dialog: MatDialog,
        private feedbackService: FeedbackService
    ) {
        this.formFoto = fb.group({
            useWebcam: false,
        });

        this.createForm(new Pessoa());
    }

    createForm(pessoa: Pessoa) {
        this.form = this.fb.group({
            nome: [pessoa.nome, Validators.required],
            informacaoAcesso: [pessoa.informacaoAcesso, Validators.required],
            grupo: pessoa.grupo,
            cpf: [{ value: pessoa.cpf, disabled: this.edicao }, Validators.required],
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
            fileFoto: null,
            id: pessoa.id
        })
        this.imageSrc = null;
    }

    public tirarFoto(): void {
        this.webcamTrigger.next();
    }

    public novaFoto(): void {
        this.imageSrc = null;
    }

    public handleImage(webcamImage: WebcamImage): void {
        this.webcamImage = webcamImage;
        this.imageSrc = this.webcamImage.imageAsDataUrl;
        this.form.get('foto').setValue(this.webcamImage.imageAsBase64);
    }

    public get triggerObservable(): Observable<void> {
        return this.webcamTrigger.asObservable();
    }

    addFiles() {
        this.file.nativeElement.click();
    }

    onFilesAdded(event: Event): void {
        const target = event.target as any;
        if (target.files && target.files[0]) {
            const file = target.files[0];

            this.form.get('fileFoto').setValue(file);
            const reader = new FileReader();
            reader.onload = e => {
                this.imageSrc = reader.result as string;
                this.form.get('foto').setValue(this.imageSrc);
            }

            reader.readAsDataURL(file);
        }
    }

    save() {
        this.btnSalvar.wait();
        const formData: FormData = new FormData();
        const param = this.form.value;
        Object.keys(param).forEach(key => {
            if (param[key] instanceof File) {
                formData.append(key, param[key], param[key].name);
            } else if (param[key] != null) {
                formData.append(key, param[key]);
            }
        });

        let request = this.edicao ? this.service.update<any, PessoaResponse>(this.backendPath, formData) : this.service.create<any, PessoaResponse>(this.backendPath, formData);
        request
            .pipe(
                finalize(() => this.btnSalvar.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage('Registro cadastrado com sucesso. Iniciando envio da pessoa para aparelhos...');
                    this.formFoto.get('useWebcam').setValue(false);

                    const dialogRef = this.dialog.open(FeedbackIntegracaoDialogComponent, {
                        width: '500px',
                        data: { integracaoId: success.integracaoId },
                    });

                    dialogRef.afterClosed().subscribe(result => {
                        if (result) {
                            this.novo();
                        }
                    });
                }
            );
    }

    pesquisar() {
        this.btnPesquisar.wait();
        this.cpfPesquisa = this.form.get('cpf').value;
        this.service.get<Pessoa>(`${this.backendPath}/${this.cpfPesquisa}`)
            .pipe(
                finalize(() => this.btnPesquisar.release())
            ).subscribe(
                result => {
                    this.edicao = true;
                    this.createForm(result);
                    this.imageSrc = `data:image/jpeg;base64,${result.foto}`;
                },
                error => this.novo()
            )
    }

    novo() {
        this.edicao = false;
        this.createForm(new Pessoa());
    }

    excluir() {
        this.btnExcluir.wait();
        this.service.delete(this.backendPath, this.form.get('id').value)
            .pipe(
                finalize(() => this.btnExcluir.release())
            ).subscribe(
                result => {
                    this.feedbackService.showSuccessMessage('Registro excluído com sucesso.');
                    console.log('exclusao', result);
                    const dialogRef = this.dialog.open(FeedbackIntegracaoDialogComponent, {
                        width: '500px',
                        data: { integracaoId: result },
                    });

                    dialogRef.afterClosed().subscribe(result => {
                        if (result) {
                            this.novo();
                        }
                    });
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
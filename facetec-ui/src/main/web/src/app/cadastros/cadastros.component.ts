import { Component, ViewChild, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

@Component({
    selector: 'app-cadastros',
    templateUrl: './cadastros.component.html',
    styleUrls: ['./cadastros.component.scss']
})
export class CadastrosComponent {

    form: FormGroup;

    informacoesAcesso: string[] = ['Permanente', 'Visitante'];

    @ViewChild('file') file;
    public files: Set<File> = new Set();
    imageSrc: string;

    constructor(private fb: FormBuilder) {
        this.form = fb.group({
            nome: ['', Validators.required],
            informacaoAcesso: ['', Validators.required],
            grupo: '',
            cpf: ['', Validators.required],
            telefone: '',
            celular: '',
            email: '',
            dataInicio: '',
            horaInicio: '',
            dataFim: '',
            horaFim: '',
            dataUltimoAcesso: [{ value: '', disabled: true }],
            horaUltimoAcesso: [{ value: '', disabled: true }],
            comentario: ''
        })
    }

    addFiles() {
        this.file.nativeElement.click();
    }

    onFilesAdded(event: Event): void {
        const target = event.target as any;
        if (target.files && target.files[0]) {
            const file = target.files[0];

            const reader = new FileReader();
            reader.onload = e => this.imageSrc = reader.result as string;

            reader.readAsDataURL(file);
        }
    }

    // onFilesAdded() {
    //     const files: { [key: string]: File } = this.file.nativeElement.files;
    //     for (const key in files) {
    //         if (!isNaN(parseInt(key))) {
    //             this.files.add(files[key]);
    //         }
    //     }
    // }

}
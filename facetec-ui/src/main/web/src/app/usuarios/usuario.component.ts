import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { AsyncButtonDirective } from "../services/async-button.directive";
import { AutocompleteComponent } from "../shared/autocomplete/autocomplete.component";
import { FacetecService } from "../services/facetec.service";
import { finalize } from "rxjs/operators";
import { FeedbackService } from "../shared/feedback.service";
import { Usuario } from "./usuario";

@Component({
    selector: 'app-facetec-usuario',
    templateUrl: './usuario.component.html',
    styleUrls: ['./usuario.component.scss']
})
export class UsuarioComponent implements OnInit {

    backendPath = 'usuario';
    form: FormGroup;

    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;

    @ViewChild('btnExcluir') btnExcluir: AsyncButtonDirective;

    @ViewChild('usernameAutocomplete') usernameAutocomplete: AutocompleteComponent;

    constructor(private fb: FormBuilder, private service: FacetecService, private feedbackService: FeedbackService) { }

    ngOnInit() {
        this.createForm(new Usuario());
    }

    createForm(usuario: Usuario) {
        this.form = this.fb.group({
            'id': usuario.id,
            'usernameOriginal': usuario.username,
            'username': [usuario.username, Validators.required],
            'admin': usuario.admin,
            'changePassword': false,
            'password': '',
            'localidade': [usuario.localidade, Validators.required]
        })
    }

    atualizarUsername() {
        const usuarioSelecionado = this.form.get('usernameOriginal').value;
        this.form.get('username').setValue(usuarioSelecionado);
        if (this.isUsuarioSelecionado()) {
            this.service.get<Usuario>(`${this.backendPath}/find/${usuarioSelecionado}`).subscribe(
                success => {
                    this.createForm(success);
                    this.form.get('password').disable();
                }
            )
        } else {
            this.createForm(new Usuario());
            this.form.get('password').enable();
        }

    }

    incluirPassword() {
        return !this.isEdicao() || this.form.get('changePassword').value;
    }

    mudarSenha() {
        if (this.form.get('changePassword').value) {
            this.form.get('password').enable();
        } else {
            this.form.get('password').disable();
        }
    }

    isEdicao() {
        return this.isUsuarioSelecionado();
    }

    isUsuarioSelecionado() {
        const userSelecionado = this.form.get('usernameOriginal');
        return userSelecionado.value !== null && userSelecionado.value.length > 0 && userSelecionado.valid;
    }

    salvar() {
        this.btnSalvar.wait();
        const request = this.isEdicao() ? this.service.update(this.backendPath, this.form.value) : this.service.create(this.backendPath, this.form.value);
        request
            .pipe(
                finalize(() => this.btnSalvar.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage("Registro cadastrado com sucesso.");
                    this.usernameAutocomplete.loadList();
                    this.createForm(new Usuario());
                    this.form.get('usernameOriginal').setValue('');
                    this.form.get('localidade').setValue('');
                }
            );
    }

    excluir() {
        this.btnExcluir.wait();
        this.service.delete(this.backendPath, this.form.get('id').value)
            .pipe(
                finalize(() => this.btnExcluir.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage("Registro excluído com sucesso.");
                    this.usernameAutocomplete.loadList();
                    this.createForm(new Usuario());
                    this.form.get('usernameOriginal').setValue('');
                }
            );
    }
}
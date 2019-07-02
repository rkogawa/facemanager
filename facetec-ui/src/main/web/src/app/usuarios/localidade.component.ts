import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { AsyncButtonDirective } from "../services/async-button.directive";
import { FacetecService } from "../services/facetec.service";
import { finalize } from "rxjs/operators";
import { FeedbackService } from "../shared/feedback.service";
import { AutocompleteComponent } from "../shared/autocomplete/autocomplete.component";

@Component({
    selector: 'app-localidade',
    templateUrl: './localidade.component.html'
})
export class LocalidadeComponent implements OnInit {

    backendPath = 'localidade';
    form: FormGroup;

    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;

    @ViewChild('btnExcluir') btnExcluir: AsyncButtonDirective;

    @ViewChild('localidadeAutocomplete') localidadeAutocomplete: AutocompleteComponent;

    constructor(private fb: FormBuilder, private service: FacetecService, private feedbackService: FeedbackService) { }

    ngOnInit() {
        this.createForm();
    }

    createForm() {
        this.form = this.fb.group({
            'nomeOriginal': '',
            'nome': ['', Validators.required]
        })
    }

    atualizarNome() {
        this.form.get('nome').setValue(this.form.get('nomeOriginal').value);
    }

    isEdicao() {
        const localidadeSelecionada = this.form.get('nomeOriginal');
        return localidadeSelecionada.value !== null && localidadeSelecionada.value.length > 0 && localidadeSelecionada.valid;
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
                    this.localidadeAutocomplete.loadList();
                    this.createForm();
                }
            );
    }

    excluir() {
        this.btnExcluir.wait();
        this.service.delete(this.backendPath, this.form.get('nomeOriginal').value)
            .pipe(
                finalize(() => this.btnExcluir.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage("Registro excluído com sucesso.");
                    this.localidadeAutocomplete.loadList();
                    this.createForm();
                }
            );
    }
}
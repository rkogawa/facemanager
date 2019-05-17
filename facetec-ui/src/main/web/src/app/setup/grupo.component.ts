import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { AsyncButtonDirective } from "../services/async-button.directive";
import { FacetecService } from "../services/facetec.service";
import { finalize } from "rxjs/operators";
import { FeedbackService } from "../shared/feedback.service";
import { AutocompleteComponent } from "../shared/autocomplete/autocomplete.component";

@Component({
    selector: 'app-grupo',
    templateUrl: './grupo.component.html',
    styleUrls: ['./grupo.component.scss']
})
export class GrupoComponent implements OnInit {

    backendPath = 'grupo';
    form: FormGroup;

    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;

    @ViewChild('btnExcluir') btnExcluir: AsyncButtonDirective;

    @ViewChild('grupoAutocomplete') grupoAutocomplete: AutocompleteComponent;

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
        const grupoSelecionado = this.form.get('nomeOriginal');
        return grupoSelecionado.value !== null && grupoSelecionado.value.length > 0 && grupoSelecionado.valid;
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
                    this.grupoAutocomplete.loadList();
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
                    this.grupoAutocomplete.loadList();
                    this.createForm();
                }
            );
    }
}
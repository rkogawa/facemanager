import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { FacetecService } from '../services/facetec.service';
import { Subscription } from 'rxjs';
import { IntegracaoPessoa } from './pessoa';
import { FeedbackService } from '../shared/feedback.service';

export interface DialogData {
  integracaoId: number;
}

@Component({
  selector: 'app-feedback-integracao-dialog',
  templateUrl: './feedback-integracao-dialog.component.html',
  styleUrls: ['./feedback-integracao-dialog.component.scss']
})
export class FeedbackIntegracaoDialogComponent implements OnInit, OnDestroy {

  private timer: Subscription;
  private backendPath = 'integracaoPessoa';

  constructor(
    public dialogRef: MatDialogRef<FeedbackIntegracaoDialogComponent>,
    private service: FacetecService,
    private feedbackService: FeedbackService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  ngOnInit() {
    this.timer = TimerObservable.create(0, 1000).subscribe(() => {
      this.service.get<IntegracaoPessoa>(`${this.backendPath}/status/${this.data.integracaoId}`).subscribe(
        result => {
          if (result.status.endsWith('OK')) {
            this.feedbackService.showSuccessMessage('Integração enviada com sucesso.');
            this.dialogRef.close(true);
          } else if (result.status.endsWith('ERRO')) {
            this.feedbackService.showErrorMessage('Erro ao enviar dados para integração. Favor verificar conexão com aparelho e salvar novamente o cadastro.');
            this.dialogRef.close();
          }
        }
      )
    });
  }

  ngOnDestroy() {
    this.timer.unsubscribe();
  }
}

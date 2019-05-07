import { ErrorHandler, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { HttpErrorResponse } from '@angular/common/http';

const httpMessages = {
  401: 'Acesso negado.',
  504: 'Não foi possível se comunicar com o servidor.',
};

@Injectable()
export class CustomErrorHandler implements ErrorHandler {

  constructor(private snackBar: MatSnackBar) { }

  public handleError(error: any) {
    if (error instanceof HttpErrorResponse) {
      if (httpMessages[error.status] == null) {
        if (error.error.message) {
          this.openSnack(error.error.message);
        } else {
          this.openSnack(error.error);
        }
        throw error;
      } else {
        this.openSnack(httpMessages[error.status]);
        throw error;
      }
    }
    this.openSnack(error);
    throw error;
  }

  private openSnack(message: string) {
    this.snackBar.open(message, ' X ', { duration: 5000, panelClass: ['error-snackbar'] });
  }
}

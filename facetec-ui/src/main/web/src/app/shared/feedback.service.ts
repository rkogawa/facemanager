import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { MatSnackBar } from '@angular/material';


@Injectable()
export class FeedbackService {

    constructor(private snackBar: MatSnackBar) { }

    showErrorMessage(message) {
        this.snackBar.open(message, ' X ', { duration: 5000, panelClass: ['error-snackbar'] });
    }

    showSuccessMessage(message) {
        this.snackBar.open(message, ' X ', { duration: 5000, panelClass: ['success-snackbar'] });
    }
}

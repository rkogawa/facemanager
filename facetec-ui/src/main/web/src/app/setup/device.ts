export class Device {
    ip: string;
    nome: string;
    classificacao: string;
}

export class FeedbackPersonDevice {
    success: Array<string> = [];
    createError: Array<string> = [];
    faceError: Array<string> = [];
    permissionError: Array<string> = [];
    permissionDelError: Array<string> = [];

    getTotalRegistros() {
        return this.success.length + this.createError.length + this.faceError.length + this.permissionError.length + this.permissionDelError.length;
    }

    hasError() {
        return this.createError.length > 0 || this.faceError.length > 0 || this.permissionError.length > 0;
    }

    getErrorMessage() {
        let message = '';
        if (this.success.length > 0) {
            message = this.getSuccessMessage();
        }
        if (this.createError.length > 0) {
            message += `Falha para criar pessoa nos aparelhos ${this.createError.join()}. `;
        }
        if (this.faceError.length > 0) {
            message += `Falha para registrar fotos nos aparelhos ${this.faceError.join()}. `;
        }
        if (this.permissionDelError.length > 0) {
            message += `Falha para excluir permissões nos aparelhos ${this.permissionDelError.join()}`;
        }
        if (this.permissionError.length > 0) {
            message += `Falha para registrar permissões nos aparelhos ${this.permissionError.join()}.`;
        }
        return message;
    }

    getSuccessMessage() {
        return `Registro enviado para os aparelhos ${this.success.join()}. `;
    }
}
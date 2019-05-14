export class Pessoa {
    id: number;
    nome = '';
    informacaoAcesso = '';
    grupo = '';
    cpf = '';
    telefone = '';
    celular = '';
    email = '';
    dataInicio = '';
    horaInicio = '';
    dataFim = '';
    horaFim = '';
    dataUltimoAcesso: string;
    horaUltimoAcesso: string;
    comentario = '';
    foto: string;
}

export class PessoaResponse {
    id: number;
    dataHoraFim: number;
    foto: string;
}
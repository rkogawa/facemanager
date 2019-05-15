import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Device, FeedbackPersonDevice } from "../setup/device";
import { FacetecService } from "./facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { Pessoa, PessoaResponse } from "../cadastros/pessoa";
import { AsyncButtonDirective } from "./async-button.directive";
import { finalize } from "rxjs/operators";

@Injectable()
export class DeviceService {

    backendPath = 'device';
    devicePassword = '12345';

    constructor(private httpClient: HttpClient, private service: FacetecService, private feedbackService: FeedbackService) {
    }

    private postDevice<I>(ip: string, path: string, params: any) {
        return this.httpClient.post<I>(`http://${ip}:8088/${path}`, params);
    }

    public getDevices() {
        return this.service.get<Device[]>('device');
    }

    public getDeviceKey<I>(ip: string): Observable<any> {
        return this.postDevice<I>(ip, 'getDeviceKey', {});
    }

    public saveDevices<I>(param: I, btnSalvar: AsyncButtonDirective) {
        this.service.create(this.backendPath, param)
            .pipe(
                finalize(() => btnSalvar.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage('Registro cadastrado com sucesso.');
                }
            );
    }

    public createPerson(pessoa: Pessoa, pessoaResponse: PessoaResponse) {
        const personCreateParams = { 'pass': this.devicePassword, 'person': { 'id': pessoa.cpf, 'name': pessoa.nome, 'idcardNum': pessoaResponse.id, 'iccardNum': '' } };
        const faceCreateParams = { 'pass': this.devicePassword, 'personId': pessoa.cpf, 'imgBase64': pessoaResponse.foto };

        const log = new FeedbackPersonDevice();
        this.getDevices().subscribe(
            devices => {
                devices.forEach(d => {
                    this.postDevice<PessoaResponse>(d.ip, 'person/create', personCreateParams)
                        .pipe(
                            finalize(() => {
                                if (log.getTotalRegistros() === devices.length) {
                                    if (log.hasError()) {
                                        this.feedbackService.showErrorMessage(log.getErrorMessage());
                                    } else {
                                        this.feedbackService.showSuccessMessage(log.getSuccessMessage());
                                    }
                                }
                            })
                        )
                        .subscribe(
                            personResult => {
                                this.registrarFoto(pessoa, d, faceCreateParams, pessoaResponse, log);
                            },
                            personError => log.createError.push(d.nome)
                        )
                });
            }
        )
    }

    private registrarFoto(pessoa: Pessoa, d: Device, faceCreateParams: any, personResult: PessoaResponse, log: FeedbackPersonDevice) {
        this.postDevice(d.ip, 'face/create', faceCreateParams).subscribe(
            faceResult => {
                this.registrarPermissao(pessoa, d, personResult, log);
            }, faceError => log.createError.push(d.nome)
        )
    }

    private registrarPermissao(pessoa: Pessoa, d: Device, personResult: PessoaResponse, log: FeedbackPersonDevice) {
        if (personResult.dataHoraFim !== null) {
            const permissionParams = { 'pass': this.devicePassword, 'personId': pessoa.cpf, 'time': personResult.dataHoraFim };
            this.postDevice(d.ip, 'person/permissionsCreate', permissionParams).subscribe(
                permissionResult => {
                    this.feedbackService.showSuccessMessage(`Visitante com nome ${pessoa.nome} registrado no device.`);
                    log.success.push(d.nome);
                }, permissionError => log.permissionError.push(d.nome)
            )
        } else {
            log.success.push(d.nome);
        }
    }

    public deletePerson(cpf: string) {
        const personDeleteParams = { 'pass': this.devicePassword, 'id': cpf };

        this.getDevices().subscribe(
            devices => {
                devices.forEach(d => {
                    this.postDevice<PessoaResponse>(d.ip, 'person/delete', personDeleteParams).subscribe(
                        personResult => {
                            this.feedbackService.showSuccessMessage(`Registro excluído com sucesso no device ${d.nome}.`);
                        },
                        personError => {
                            this.feedbackService.showErrorMessage(`Erro na exclusão do registro no device ${d.nome}: ${personError.message}`);
                        }
                    )
                });
            });
    }
}
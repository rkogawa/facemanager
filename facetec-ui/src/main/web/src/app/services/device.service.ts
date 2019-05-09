import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Device } from "../setup/device";
import { FacetecService } from "./facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { Pessoa, PessoaResponse } from "../cadastros/pessoa";

@Injectable()
export class DeviceService {

    backendPath = 'device';
    devicePassword = '12345';
    devices: Array<Device> = [];

    constructor(private httpClient: HttpClient, private service: FacetecService, private feedbackService: FeedbackService) {
        this.loadDevices();
    }

    private postDevice<I>(ip: string, path: string, params: any) {
        return this.httpClient.post<I>(`http://${ip}:8088/${path}`, params);
    }

    private loadDevices() {
        this.service.get<Device[]>('device').subscribe(
            response => this.devices = response
        )
    }

    public getDeviceKey<I>(ip: string): Observable<any> {
        return this.postDevice<I>(ip, 'getDeviceKey', {});
    }

    public saveDevices<I>(param: I) {
        this.service.create(this.backendPath, param).subscribe(
            success => {
                this.loadDevices();
                this.feedbackService.showSuccessMessage('Registro cadastrado com sucesso.');
            }
        );
    }

    public createPerson(pessoa: Pessoa) {
        const personCreateParams = { 'pass': this.devicePassword, 'person': { 'id': pessoa.cpf, 'name': pessoa.nome } };
        const faceCreateParams = { 'pass': this.devicePassword, 'personId': pessoa.cpf, 'imgBase64': pessoa.foto };

        this.devices.forEach(d => {
            this.postDevice<PessoaResponse>(d.ip, 'person/create', personCreateParams).subscribe(
                personResult => {
                    console.log(`\nRegistro criado com sucesso no device ${d.nome}.`);
                    this.registrarFoto(pessoa, d, faceCreateParams, personResult);
                },
                personError => {
                    console.log(`\nRegistro não criado no device ${d.nome}: ${personError.message}`);
                }
            )
        });
    }

    public deletePerson(cpf: string) {
        const personDeleteParams = { 'pass': this.devicePassword, 'id': cpf };

        this.devices.forEach(d => {
            this.postDevice<PessoaResponse>(d.ip, 'person/delete', personDeleteParams).subscribe(
                personResult => {
                    console.log(`\nRegistro excluido com sucesso no device ${d.nome}.`);
                },
                personError => {
                    console.log(`\nRegistro não excluido no device ${d.nome}: ${personError.message}`);
                }
            )
        });
    }

    private registrarFoto(pessoa: Pessoa, d: Device, faceCreateParams: any, personResult: PessoaResponse) {
        this.postDevice(d.ip, 'face/create', faceCreateParams).subscribe(
            faceResult => {
                console.log(`\nFoto registrada com sucesso.`);
                this.registrarPermissao(pessoa, d, personResult);
            }, faceError => console.log(`\nFoto não registrado no device ${d.nome}: ${faceError.message}`)
        )
    }

    private registrarPermissao(pessoa: Pessoa, d: Device, personResult: PessoaResponse) {
        if (personResult.dataHoraFim !== null) {
            const permissionParams = { 'pass': this.devicePassword, 'personId': pessoa.cpf, 'time': personResult.dataHoraFim };
            this.postDevice(d.ip, 'person/permissionsCreate', permissionParams).subscribe(
                permissionResult => {
                    console.log(`\nPermissão registrada no device ${d.nome}`);
                }, permissionError => {
                    console.log(`\nPermissão não registrada no device ${d.nome}: ${permissionError.message}`);
                }
            )
        }
    }
}
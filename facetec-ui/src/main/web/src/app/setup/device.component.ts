import { Component, ViewChild, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { FacetecService } from "../services/facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { DeviceService } from "../services/device.service";
import { MatTableDataSource } from "@angular/material";
import { Device } from "./device";
import { finalize, max } from "rxjs/operators";
import { AsyncButtonDirective } from "../services/async-button.directive";

@Component({
    selector: 'app-device',
    templateUrl: './device.component.html',
    styleUrls: ['./device.component.scss']
})
export class DeviceComponent implements OnInit {

    backendPath = 'device';
    formPesquisa: FormGroup;
    form: FormGroup;
    devices: Array<Device> = [];

    dataSource = new MatTableDataSource();

    @ViewChild('btnPesquisar') btnPesquisar: AsyncButtonDirective;
    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;
    ipVerificado: string;

    displayedColumns = ['ip', 'nome', 'classificacao'];
    constructor(
        private fb: FormBuilder,
        private deviceService: DeviceService,
        private feedbackService: FeedbackService
    ) {
    }

    ngOnInit() {
        this.formPesquisa = this.fb.group({
            dominio: ''
        })
        this.form = this.fb.group({
            adminPassword: ['', Validators.required],
            devices: []
        })

        this.deviceService.getDevices().subscribe(devices => {
            this.devices = devices;
            this.updateDevices(this.devices);
        });
    }

    private updateDevices(devices: Array<Device>) {
        this.dataSource = new MatTableDataSource(devices);
        const devicesFGs = [];
        devices.forEach(device => {
            devicesFGs.push(this.fb.group({
                ip: device.ip,
                nome: device.nome,
                classificacao: device.classificacao
            }));
        });
        this.form.setControl('devices', this.fb.array(devicesFGs));
    }

    private getOrCreateDevice(ip: string) {
        for (let i = 0; i < this.devices.length; i++) {
            if (this.devices[i].ip === ip) {
                return this.devices[i];
            }
        }
        const device = new Device();
        device.ip = ip;
        return device;
    }

    pesquisar() {
        let countIpsVerificados = 0;
        const maxIp = 256;
        const devices = [];
        this.btnPesquisar.wait();
        for (let i = 0; i < maxIp; i++) {
            const ip = `${this.formPesquisa.get('dominio').value}.${i}`;

            this.deviceService.getDeviceKey(ip)
                .pipe(
                    finalize(() => {
                        this.ipVerificado = ip;
                        countIpsVerificados++;
                        if (countIpsVerificados === maxIp) {
                            this.updateDevices(devices);
                            this.btnPesquisar.release();
                            if (devices.length === 0) {
                                this.feedbackService.showErrorMessage('Não foi encontrado nenhum aparelho para este domínio.');
                            }
                        }
                    })
                ).subscribe(
                    success => {
                        devices.push(this.getOrCreateDevice(ip));
                    }
                )
        }
    }

    salvar() {
        this.btnSalvar.wait();
        this.deviceService.saveDevices(this.form.value, this.btnSalvar);
    }
}

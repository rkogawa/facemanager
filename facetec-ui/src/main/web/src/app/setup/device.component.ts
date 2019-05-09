import { Component } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { FacetecService } from "../services/facetec.service";
import { FeedbackService } from "../shared/feedback.service";
import { DeviceService } from "../services/device.service";
import { MatTableDataSource } from "@angular/material";
import { Device } from "./device";
import { finalize, max } from "rxjs/operators";

@Component({
    selector: 'app-device',
    templateUrl: './device.component.html',
    styleUrls: ['./device.component.scss']
})
export class DeviceComponent {

    form: FormGroup;

    dataSource = new MatTableDataSource();

    displayedColumns = ['ip', 'nome', 'classificacao'];
    constructor(
        private fb: FormBuilder,
        private service: FacetecService,
        private deviceService: DeviceService,
        private feedbackService: FeedbackService
    ) {
        this.form = this.fb.group({
            dominio: '',
            devices: []
        })
        this.form.setControl('devices', this.fb.array([]));
    }

    pesquisar() {
        let countIpsVerificados = 0;
        const maxIp = 256;
        const devices = [];
        for (let i = 0; i < maxIp; i++) {
            const ip = `${this.form.get('dominio').value}.${i}`;
            this.deviceService.getDeviceKey(ip)
                .pipe(
                    finalize(() => {
                        countIpsVerificados++;
                        if (countIpsVerificados === maxIp) {
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
                    })
                ).subscribe(
                    success => {
                        // const device = new Device();
                        // device.ip = ip;
                        // this.dataSource.data.push(device);
                    },
                    error => {
                        const device = new Device();
                        device.ip = ip;
                        devices.push(device);
                        // console.log(ip);
                        // this.dataSource.data.push(device);
                    }
                )
        }
    }

}

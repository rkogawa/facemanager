import { Component, ViewChild, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators, FormArray } from "@angular/forms";
import { FacetecService } from "../services/facetec.service";
import { FeedbackService } from "../shared/feedback.service";
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
    form: FormGroup;
    devices: Array<Device> = [];

    dataSource = new MatTableDataSource();

    @ViewChild('btnSalvar') btnSalvar: AsyncButtonDirective;

    displayedColumns = ['ip', 'nome', 'classificacao', 'modelo', 'remove'];
    constructor(
        private fb: FormBuilder,
        private feedbackService: FeedbackService,
        private service: FacetecService
    ) {
    }

    ngOnInit() {
        this.dataSource = new MatTableDataSource(this.devices);
        this.form = this.fb.group({
            adminPassword: ['', Validators.required],
            devices: []
        })

        this.service.get<Device[]>('device').subscribe(devices => {
            this.devices = devices;
            this.updateDevices(this.devices);
        });
    }

    private updateDevices(devices: Array<Device>) {
        this.dataSource = new MatTableDataSource(devices);
        const devicesFGs = [];
        devices.forEach(device => {
            devicesFGs.push(this.createDeviceFormGroup(device));
        });
        this.form.setControl('devices', this.fb.array(devicesFGs));
    }

    private createDeviceFormGroup(device: Device) {
        return this.fb.group({
            ip: device.ip,
            nome: device.nome,
            classificacao: [device.classificacao, Validators.required],
            modelo: [device.modelo, Validators.required]
        })
    }

    novo() {
        const devices = <FormArray>this.form.controls['devices'];
        devices.push(this.createDeviceFormGroup(new Device()));
        this.dataSource = new MatTableDataSource(devices.value);
    }

    excluir(index: number) {
        const devices = <FormArray>this.form.controls['devices'];
        devices.removeAt(index);
        this.dataSource = new MatTableDataSource(devices.value);
    }

    salvar() {
        this.btnSalvar.wait();
        this.service.create(this.backendPath, this.form.value)
            .pipe(
                finalize(() => this.btnSalvar.release())
            ).subscribe(
                success => {
                    this.feedbackService.showSuccessMessage('Registro cadastrado com sucesso.');
                }
            );
    }
}

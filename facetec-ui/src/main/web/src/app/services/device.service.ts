import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class DeviceService {

    constructor(private httpClient: HttpClient) {
    }

    public getDeviceKey(ip: string): Observable<any> {
        return this.httpClient.post<any>(`http://${ip}:8088/getDeviceKey`, {});
    }

}
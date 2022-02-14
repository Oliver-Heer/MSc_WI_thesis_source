import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from "@angular/common/http";
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-procurement',
  templateUrl: './procurement.component.html',
  styleUrls: ['./procurement.component.scss']
})
export class ProcurementComponent {

  constructor(private httpClient: HttpClient) { }

  procure() {
    var params = new HttpParams()
      .set('url', 'https://www.opernhaus.ch/xmlexport/kzexport.xml');
  
    this.httpClient.get(environment.backendUrl + "/procure", {params})
      .subscribe(response => {
        console.log(response);
      });
  }
}

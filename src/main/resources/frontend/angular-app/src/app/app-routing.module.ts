import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProcurementComponent } from './procurement/procurement.component';

const routes: Routes = [
  { path: '', component: ProcurementComponent },
  { path: 'procurement', component: ProcurementComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

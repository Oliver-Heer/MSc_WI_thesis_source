import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProcurementComponent } from './procurement/procurement.component';
import { HttpClientModule } from '@angular/common/http';

// Material
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';

//Components
import { MainViewComponent } from './main-view/main-view.component';
import { ReconciliationComponent } from './reconciliation/reconciliation.component';
import { PublicationComponent } from './publication/publication.component';
import { PreparationComponent } from './preparation/preparation.component'; 

@NgModule({
  declarations: [
    AppComponent,
    ProcurementComponent,
    MainViewComponent,
    ReconciliationComponent,
    PublicationComponent,
    PreparationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatTabsModule,
    MatCardModule,
    MatExpansionModule,
    MatGridListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

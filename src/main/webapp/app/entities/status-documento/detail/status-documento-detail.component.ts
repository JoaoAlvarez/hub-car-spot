import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IStatusDocumento } from '../status-documento.model';

@Component({
  standalone: true,
  selector: 'jhi-status-documento-detail',
  templateUrl: './status-documento-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StatusDocumentoDetailComponent {
  statusDocumento = input<IStatusDocumento | null>(null);

  previousState(): void {
    window.history.back();
  }
}

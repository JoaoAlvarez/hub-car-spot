import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITaxas } from '../taxas.model';

@Component({
  standalone: true,
  selector: 'jhi-taxas-detail',
  templateUrl: './taxas-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TaxasDetailComponent {
  taxas = input<ITaxas | null>(null);

  previousState(): void {
    window.history.back();
  }
}

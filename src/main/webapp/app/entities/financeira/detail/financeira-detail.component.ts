import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IFinanceira } from '../financeira.model';

@Component({
  standalone: true,
  selector: 'jhi-financeira-detail',
  templateUrl: './financeira-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FinanceiraDetailComponent {
  financeira = input<IFinanceira | null>(null);

  previousState(): void {
    window.history.back();
  }
}

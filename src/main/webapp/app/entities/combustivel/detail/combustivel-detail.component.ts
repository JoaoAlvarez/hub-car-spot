import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICombustivel } from '../combustivel.model';

@Component({
  standalone: true,
  selector: 'jhi-combustivel-detail',
  templateUrl: './combustivel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CombustivelDetailComponent {
  combustivel = input<ICombustivel | null>(null);

  previousState(): void {
    window.history.back();
  }
}

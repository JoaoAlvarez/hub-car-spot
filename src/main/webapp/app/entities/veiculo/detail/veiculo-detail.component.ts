import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVeiculo } from '../veiculo.model';

@Component({
  standalone: true,
  selector: 'jhi-veiculo-detail',
  templateUrl: './veiculo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VeiculoDetailComponent {
  veiculo = input<IVeiculo | null>(null);

  previousState(): void {
    window.history.back();
  }
}

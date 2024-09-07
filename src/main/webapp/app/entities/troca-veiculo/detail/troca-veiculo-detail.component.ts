import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrocaVeiculo } from '../troca-veiculo.model';

@Component({
  standalone: true,
  selector: 'jhi-troca-veiculo-detail',
  templateUrl: './troca-veiculo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TrocaVeiculoDetailComponent {
  trocaVeiculo = input<ITrocaVeiculo | null>(null);

  previousState(): void {
    window.history.back();
  }
}

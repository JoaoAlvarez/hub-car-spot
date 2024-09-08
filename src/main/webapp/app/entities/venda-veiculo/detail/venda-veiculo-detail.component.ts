import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVendaVeiculo } from '../venda-veiculo.model';

@Component({
  standalone: true,
  selector: 'jhi-venda-veiculo-detail',
  templateUrl: './venda-veiculo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VendaVeiculoDetailComponent {
  vendaVeiculo = input<IVendaVeiculo | null>(null);

  previousState(): void {
    window.history.back();
  }
}

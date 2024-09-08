import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICompraVeiculo } from '../compra-veiculo.model';

@Component({
  standalone: true,
  selector: 'jhi-compra-veiculo-detail',
  templateUrl: './compra-veiculo-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CompraVeiculoDetailComponent {
  compraVeiculo = input<ICompraVeiculo | null>(null);

  previousState(): void {
    window.history.back();
  }
}

import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrocaVeiculo } from '../troca-veiculo.model';
import { TrocaVeiculoService } from '../service/troca-veiculo.service';

@Component({
  standalone: true,
  templateUrl: './troca-veiculo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrocaVeiculoDeleteDialogComponent {
  trocaVeiculo?: ITrocaVeiculo;

  protected trocaVeiculoService = inject(TrocaVeiculoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.trocaVeiculoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

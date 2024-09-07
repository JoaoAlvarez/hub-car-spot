import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompraVeiculo } from '../compra-veiculo.model';
import { CompraVeiculoService } from '../service/compra-veiculo.service';

@Component({
  standalone: true,
  templateUrl: './compra-veiculo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CompraVeiculoDeleteDialogComponent {
  compraVeiculo?: ICompraVeiculo;

  protected compraVeiculoService = inject(CompraVeiculoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.compraVeiculoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

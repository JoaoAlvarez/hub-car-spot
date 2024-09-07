import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVendaVeiculo } from '../venda-veiculo.model';
import { VendaVeiculoService } from '../service/venda-veiculo.service';

@Component({
  standalone: true,
  templateUrl: './venda-veiculo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VendaVeiculoDeleteDialogComponent {
  vendaVeiculo?: IVendaVeiculo;

  protected vendaVeiculoService = inject(VendaVeiculoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.vendaVeiculoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

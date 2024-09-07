import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVeiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';

@Component({
  standalone: true,
  templateUrl: './veiculo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VeiculoDeleteDialogComponent {
  veiculo?: IVeiculo;

  protected veiculoService = inject(VeiculoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.veiculoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

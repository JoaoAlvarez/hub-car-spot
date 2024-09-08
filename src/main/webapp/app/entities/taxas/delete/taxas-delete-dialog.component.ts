import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITaxas } from '../taxas.model';
import { TaxasService } from '../service/taxas.service';

@Component({
  standalone: true,
  templateUrl: './taxas-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TaxasDeleteDialogComponent {
  taxas?: ITaxas;

  protected taxasService = inject(TaxasService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.taxasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

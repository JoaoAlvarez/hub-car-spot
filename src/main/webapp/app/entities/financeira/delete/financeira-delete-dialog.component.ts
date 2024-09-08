import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFinanceira } from '../financeira.model';
import { FinanceiraService } from '../service/financeira.service';

@Component({
  standalone: true,
  templateUrl: './financeira-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinanceiraDeleteDialogComponent {
  financeira?: IFinanceira;

  protected financeiraService = inject(FinanceiraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.financeiraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

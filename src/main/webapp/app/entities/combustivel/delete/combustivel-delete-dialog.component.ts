import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICombustivel } from '../combustivel.model';
import { CombustivelService } from '../service/combustivel.service';

@Component({
  standalone: true,
  templateUrl: './combustivel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CombustivelDeleteDialogComponent {
  combustivel?: ICombustivel;

  protected combustivelService = inject(CombustivelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.combustivelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFilial } from '../filial.model';
import { FilialService } from '../service/filial.service';

@Component({
  standalone: true,
  templateUrl: './filial-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FilialDeleteDialogComponent {
  filial?: IFilial;

  protected filialService = inject(FilialService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.filialService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

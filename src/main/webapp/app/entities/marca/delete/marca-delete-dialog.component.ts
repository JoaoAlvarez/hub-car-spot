import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMarca } from '../marca.model';
import { MarcaService } from '../service/marca.service';

@Component({
  standalone: true,
  templateUrl: './marca-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MarcaDeleteDialogComponent {
  marca?: IMarca;

  protected marcaService = inject(MarcaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.marcaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

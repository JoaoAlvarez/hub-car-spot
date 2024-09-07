import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStatusDocumento } from '../status-documento.model';
import { StatusDocumentoService } from '../service/status-documento.service';

@Component({
  standalone: true,
  templateUrl: './status-documento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StatusDocumentoDeleteDialogComponent {
  statusDocumento?: IStatusDocumento;

  protected statusDocumentoService = inject(StatusDocumentoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.statusDocumentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

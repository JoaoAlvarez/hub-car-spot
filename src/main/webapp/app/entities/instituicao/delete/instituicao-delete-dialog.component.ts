import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInstituicao } from '../instituicao.model';
import { InstituicaoService } from '../service/instituicao.service';

@Component({
  standalone: true,
  templateUrl: './instituicao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InstituicaoDeleteDialogComponent {
  instituicao?: IInstituicao;

  protected instituicaoService = inject(InstituicaoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.instituicaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

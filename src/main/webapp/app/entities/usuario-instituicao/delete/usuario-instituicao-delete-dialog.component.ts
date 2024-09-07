import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUsuarioInstituicao } from '../usuario-instituicao.model';
import { UsuarioInstituicaoService } from '../service/usuario-instituicao.service';

@Component({
  standalone: true,
  templateUrl: './usuario-instituicao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UsuarioInstituicaoDeleteDialogComponent {
  usuarioInstituicao?: IUsuarioInstituicao;

  protected usuarioInstituicaoService = inject(UsuarioInstituicaoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.usuarioInstituicaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

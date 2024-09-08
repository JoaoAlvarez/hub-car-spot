import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUsuarioInstituicao } from '../usuario-instituicao.model';

@Component({
  standalone: true,
  selector: 'jhi-usuario-instituicao-detail',
  templateUrl: './usuario-instituicao-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UsuarioInstituicaoDetailComponent {
  usuarioInstituicao = input<IUsuarioInstituicao | null>(null);

  previousState(): void {
    window.history.back();
  }
}

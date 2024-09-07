import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IInstituicao } from '../instituicao.model';

@Component({
  standalone: true,
  selector: 'jhi-instituicao-detail',
  templateUrl: './instituicao-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InstituicaoDetailComponent {
  instituicao = input<IInstituicao | null>(null);

  previousState(): void {
    window.history.back();
  }
}

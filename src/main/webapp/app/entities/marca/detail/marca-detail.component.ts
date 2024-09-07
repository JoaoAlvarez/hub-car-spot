import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMarca } from '../marca.model';

@Component({
  standalone: true,
  selector: 'jhi-marca-detail',
  templateUrl: './marca-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MarcaDetailComponent {
  marca = input<IMarca | null>(null);

  previousState(): void {
    window.history.back();
  }
}

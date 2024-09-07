import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICombustivel } from '../combustivel.model';
import { CombustivelService } from '../service/combustivel.service';
import { CombustivelFormGroup, CombustivelFormService } from './combustivel-form.service';

@Component({
  standalone: true,
  selector: 'jhi-combustivel-update',
  templateUrl: './combustivel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CombustivelUpdateComponent implements OnInit {
  isSaving = false;
  combustivel: ICombustivel | null = null;

  protected combustivelService = inject(CombustivelService);
  protected combustivelFormService = inject(CombustivelFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CombustivelFormGroup = this.combustivelFormService.createCombustivelFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ combustivel }) => {
      this.combustivel = combustivel;
      if (combustivel) {
        this.updateForm(combustivel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const combustivel = this.combustivelFormService.getCombustivel(this.editForm);
    if (combustivel.id !== null) {
      this.subscribeToSaveResponse(this.combustivelService.update(combustivel));
    } else {
      this.subscribeToSaveResponse(this.combustivelService.create(combustivel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICombustivel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(combustivel: ICombustivel): void {
    this.combustivel = combustivel;
    this.combustivelFormService.resetForm(this.editForm, combustivel);
  }
}

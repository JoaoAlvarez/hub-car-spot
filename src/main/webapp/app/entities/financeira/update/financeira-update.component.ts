import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IFinanceira } from '../financeira.model';
import { FinanceiraService } from '../service/financeira.service';
import { FinanceiraFormGroup, FinanceiraFormService } from './financeira-form.service';

@Component({
  standalone: true,
  selector: 'jhi-financeira-update',
  templateUrl: './financeira-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinanceiraUpdateComponent implements OnInit {
  isSaving = false;
  financeira: IFinanceira | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected financeiraService = inject(FinanceiraService);
  protected financeiraFormService = inject(FinanceiraFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FinanceiraFormGroup = this.financeiraFormService.createFinanceiraFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financeira }) => {
      this.financeira = financeira;
      if (financeira) {
        this.updateForm(financeira);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financeira = this.financeiraFormService.getFinanceira(this.editForm);
    if (financeira.id !== null) {
      this.subscribeToSaveResponse(this.financeiraService.update(financeira));
    } else {
      this.subscribeToSaveResponse(this.financeiraService.create(financeira));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinanceira>>): void {
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

  protected updateForm(financeira: IFinanceira): void {
    this.financeira = financeira;
    this.financeiraFormService.resetForm(this.editForm, financeira);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      financeira.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.financeira?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

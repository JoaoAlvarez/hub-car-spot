import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { ITaxas } from '../taxas.model';
import { TaxasService } from '../service/taxas.service';
import { TaxasFormGroup, TaxasFormService } from './taxas-form.service';

@Component({
  standalone: true,
  selector: 'jhi-taxas-update',
  templateUrl: './taxas-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TaxasUpdateComponent implements OnInit {
  isSaving = false;
  taxas: ITaxas | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected taxasService = inject(TaxasService);
  protected taxasFormService = inject(TaxasFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaxasFormGroup = this.taxasFormService.createTaxasFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxas }) => {
      this.taxas = taxas;
      if (taxas) {
        this.updateForm(taxas);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taxas = this.taxasFormService.getTaxas(this.editForm);
    if (taxas.id !== null) {
      this.subscribeToSaveResponse(this.taxasService.update(taxas));
    } else {
      this.subscribeToSaveResponse(this.taxasService.create(taxas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxas>>): void {
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

  protected updateForm(taxas: ITaxas): void {
    this.taxas = taxas;
    this.taxasFormService.resetForm(this.editForm, taxas);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      taxas.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.taxas?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

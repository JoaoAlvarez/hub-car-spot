import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IFornecedor } from '../fornecedor.model';
import { FornecedorService } from '../service/fornecedor.service';
import { FornecedorFormGroup, FornecedorFormService } from './fornecedor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-fornecedor-update',
  templateUrl: './fornecedor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FornecedorUpdateComponent implements OnInit {
  isSaving = false;
  fornecedor: IFornecedor | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected fornecedorService = inject(FornecedorService);
  protected fornecedorFormService = inject(FornecedorFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FornecedorFormGroup = this.fornecedorFormService.createFornecedorFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fornecedor }) => {
      this.fornecedor = fornecedor;
      if (fornecedor) {
        this.updateForm(fornecedor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fornecedor = this.fornecedorFormService.getFornecedor(this.editForm);
    if (fornecedor.id !== null) {
      this.subscribeToSaveResponse(this.fornecedorService.update(fornecedor));
    } else {
      this.subscribeToSaveResponse(this.fornecedorService.create(fornecedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFornecedor>>): void {
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

  protected updateForm(fornecedor: IFornecedor): void {
    this.fornecedor = fornecedor;
    this.fornecedorFormService.resetForm(this.editForm, fornecedor);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      fornecedor.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.fornecedor?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IFilial } from '../filial.model';
import { FilialService } from '../service/filial.service';
import { FilialFormGroup, FilialFormService } from './filial-form.service';

@Component({
  standalone: true,
  selector: 'jhi-filial-update',
  templateUrl: './filial-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FilialUpdateComponent implements OnInit {
  isSaving = false;
  filial: IFilial | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected filialService = inject(FilialService);
  protected filialFormService = inject(FilialFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FilialFormGroup = this.filialFormService.createFilialFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filial }) => {
      this.filial = filial;
      if (filial) {
        this.updateForm(filial);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filial = this.filialFormService.getFilial(this.editForm);
    if (filial.id !== null) {
      this.subscribeToSaveResponse(this.filialService.update(filial));
    } else {
      this.subscribeToSaveResponse(this.filialService.create(filial));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilial>>): void {
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

  protected updateForm(filial: IFilial): void {
    this.filial = filial;
    this.filialFormService.resetForm(this.editForm, filial);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      filial.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.filial?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

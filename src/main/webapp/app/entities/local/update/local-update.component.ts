import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { ILocal } from '../local.model';
import { LocalService } from '../service/local.service';
import { LocalFormGroup, LocalFormService } from './local-form.service';

@Component({
  standalone: true,
  selector: 'jhi-local-update',
  templateUrl: './local-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocalUpdateComponent implements OnInit {
  isSaving = false;
  local: ILocal | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected localService = inject(LocalService);
  protected localFormService = inject(LocalFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocalFormGroup = this.localFormService.createLocalFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ local }) => {
      this.local = local;
      if (local) {
        this.updateForm(local);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const local = this.localFormService.getLocal(this.editForm);
    if (local.id !== null) {
      this.subscribeToSaveResponse(this.localService.update(local));
    } else {
      this.subscribeToSaveResponse(this.localService.create(local));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocal>>): void {
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

  protected updateForm(local: ILocal): void {
    this.local = local;
    this.localFormService.resetForm(this.editForm, local);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      local.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.local?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

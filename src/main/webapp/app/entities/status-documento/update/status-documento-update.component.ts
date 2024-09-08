import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IStatusDocumento } from '../status-documento.model';
import { StatusDocumentoService } from '../service/status-documento.service';
import { StatusDocumentoFormGroup, StatusDocumentoFormService } from './status-documento-form.service';

@Component({
  standalone: true,
  selector: 'jhi-status-documento-update',
  templateUrl: './status-documento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StatusDocumentoUpdateComponent implements OnInit {
  isSaving = false;
  statusDocumento: IStatusDocumento | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected statusDocumentoService = inject(StatusDocumentoService);
  protected statusDocumentoFormService = inject(StatusDocumentoFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatusDocumentoFormGroup = this.statusDocumentoFormService.createStatusDocumentoFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusDocumento }) => {
      this.statusDocumento = statusDocumento;
      if (statusDocumento) {
        this.updateForm(statusDocumento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statusDocumento = this.statusDocumentoFormService.getStatusDocumento(this.editForm);
    if (statusDocumento.id !== null) {
      this.subscribeToSaveResponse(this.statusDocumentoService.update(statusDocumento));
    } else {
      this.subscribeToSaveResponse(this.statusDocumentoService.create(statusDocumento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatusDocumento>>): void {
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

  protected updateForm(statusDocumento: IStatusDocumento): void {
    this.statusDocumento = statusDocumento;
    this.statusDocumentoFormService.resetForm(this.editForm, statusDocumento);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      statusDocumento.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.statusDocumento?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

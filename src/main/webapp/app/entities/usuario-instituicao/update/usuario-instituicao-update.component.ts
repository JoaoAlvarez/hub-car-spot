import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IUsuarioInstituicao } from '../usuario-instituicao.model';
import { UsuarioInstituicaoService } from '../service/usuario-instituicao.service';
import { UsuarioInstituicaoFormGroup, UsuarioInstituicaoFormService } from './usuario-instituicao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-usuario-instituicao-update',
  templateUrl: './usuario-instituicao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UsuarioInstituicaoUpdateComponent implements OnInit {
  isSaving = false;
  usuarioInstituicao: IUsuarioInstituicao | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];

  protected usuarioInstituicaoService = inject(UsuarioInstituicaoService);
  protected usuarioInstituicaoFormService = inject(UsuarioInstituicaoFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UsuarioInstituicaoFormGroup = this.usuarioInstituicaoFormService.createUsuarioInstituicaoFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioInstituicao }) => {
      this.usuarioInstituicao = usuarioInstituicao;
      if (usuarioInstituicao) {
        this.updateForm(usuarioInstituicao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarioInstituicao = this.usuarioInstituicaoFormService.getUsuarioInstituicao(this.editForm);
    if (usuarioInstituicao.id !== null) {
      this.subscribeToSaveResponse(this.usuarioInstituicaoService.update(usuarioInstituicao));
    } else {
      this.subscribeToSaveResponse(this.usuarioInstituicaoService.create(usuarioInstituicao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioInstituicao>>): void {
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

  protected updateForm(usuarioInstituicao: IUsuarioInstituicao): void {
    this.usuarioInstituicao = usuarioInstituicao;
    this.usuarioInstituicaoFormService.resetForm(this.editForm, usuarioInstituicao);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      usuarioInstituicao.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.usuarioInstituicao?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from '../instituicao.model';
import { InstituicaoService } from '../service/instituicao.service';
import { InstituicaoFormGroup, InstituicaoFormService } from './instituicao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-instituicao-update',
  templateUrl: './instituicao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InstituicaoUpdateComponent implements OnInit {
  isSaving = false;
  instituicao: IInstituicao | null = null;

  protected instituicaoService = inject(InstituicaoService);
  protected instituicaoFormService = inject(InstituicaoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InstituicaoFormGroup = this.instituicaoFormService.createInstituicaoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instituicao }) => {
      this.instituicao = instituicao;
      if (instituicao) {
        this.updateForm(instituicao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instituicao = this.instituicaoFormService.getInstituicao(this.editForm);
    if (instituicao.id !== null) {
      this.subscribeToSaveResponse(this.instituicaoService.update(instituicao));
    } else {
      this.subscribeToSaveResponse(this.instituicaoService.create(instituicao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstituicao>>): void {
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

  protected updateForm(instituicao: IInstituicao): void {
    this.instituicao = instituicao;
    this.instituicaoFormService.resetForm(this.editForm, instituicao);
  }
}

import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EspecieVeiculo } from 'app/entities/enumerations/especie-veiculo.model';
import { StatusVeiculo } from 'app/entities/enumerations/status-veiculo.model';
import { IVeiculo } from '../veiculo.model';
import { VeiculoService } from '../service/veiculo.service';
import { VeiculoFormGroup, VeiculoFormService } from './veiculo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-veiculo-update',
  templateUrl: './veiculo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VeiculoUpdateComponent implements OnInit {
  isSaving = false;
  veiculo: IVeiculo | null = null;
  especieVeiculoValues = Object.keys(EspecieVeiculo);
  statusVeiculoValues = Object.keys(StatusVeiculo);

  protected veiculoService = inject(VeiculoService);
  protected veiculoFormService = inject(VeiculoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VeiculoFormGroup = this.veiculoFormService.createVeiculoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ veiculo }) => {
      this.veiculo = veiculo;
      if (veiculo) {
        this.updateForm(veiculo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const veiculo = this.veiculoFormService.getVeiculo(this.editForm);
    if (veiculo.id !== null) {
      this.subscribeToSaveResponse(this.veiculoService.update(veiculo));
    } else {
      this.subscribeToSaveResponse(this.veiculoService.create(veiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVeiculo>>): void {
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

  protected updateForm(veiculo: IVeiculo): void {
    this.veiculo = veiculo;
    this.veiculoFormService.resetForm(this.editForm, veiculo);
  }
}

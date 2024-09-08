import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { TrocaVeiculoService } from '../service/troca-veiculo.service';
import { ITrocaVeiculo } from '../troca-veiculo.model';
import { TrocaVeiculoFormGroup, TrocaVeiculoFormService } from './troca-veiculo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-troca-veiculo-update',
  templateUrl: './troca-veiculo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrocaVeiculoUpdateComponent implements OnInit {
  isSaving = false;
  trocaVeiculo: ITrocaVeiculo | null = null;

  veiculosSharedCollection: IVeiculo[] = [];
  filialsSharedCollection: IFilial[] = [];
  instituicaosSharedCollection: IInstituicao[] = [];

  protected trocaVeiculoService = inject(TrocaVeiculoService);
  protected trocaVeiculoFormService = inject(TrocaVeiculoFormService);
  protected veiculoService = inject(VeiculoService);
  protected filialService = inject(FilialService);
  protected instituicaoService = inject(InstituicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrocaVeiculoFormGroup = this.trocaVeiculoFormService.createTrocaVeiculoFormGroup();

  compareVeiculo = (o1: IVeiculo | null, o2: IVeiculo | null): boolean => this.veiculoService.compareVeiculo(o1, o2);

  compareFilial = (o1: IFilial | null, o2: IFilial | null): boolean => this.filialService.compareFilial(o1, o2);

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trocaVeiculo }) => {
      this.trocaVeiculo = trocaVeiculo;
      if (trocaVeiculo) {
        this.updateForm(trocaVeiculo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trocaVeiculo = this.trocaVeiculoFormService.getTrocaVeiculo(this.editForm);
    if (trocaVeiculo.id !== null) {
      this.subscribeToSaveResponse(this.trocaVeiculoService.update(trocaVeiculo));
    } else {
      this.subscribeToSaveResponse(this.trocaVeiculoService.create(trocaVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrocaVeiculo>>): void {
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

  protected updateForm(trocaVeiculo: ITrocaVeiculo): void {
    this.trocaVeiculo = trocaVeiculo;
    this.trocaVeiculoFormService.resetForm(this.editForm, trocaVeiculo);

    this.veiculosSharedCollection = this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(
      this.veiculosSharedCollection,
      trocaVeiculo.veiculoEntrada,
      trocaVeiculo.veiculoSaida,
    );
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing<IFilial>(
      this.filialsSharedCollection,
      trocaVeiculo.filial,
    );
    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      trocaVeiculo.instituicao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.veiculoService
      .query()
      .pipe(map((res: HttpResponse<IVeiculo[]>) => res.body ?? []))
      .pipe(
        map((veiculos: IVeiculo[]) =>
          this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(
            veiculos,
            this.trocaVeiculo?.veiculoEntrada,
            this.trocaVeiculo?.veiculoSaida,
          ),
        ),
      )
      .subscribe((veiculos: IVeiculo[]) => (this.veiculosSharedCollection = veiculos));

    this.filialService
      .query()
      .pipe(map((res: HttpResponse<IFilial[]>) => res.body ?? []))
      .pipe(map((filials: IFilial[]) => this.filialService.addFilialToCollectionIfMissing<IFilial>(filials, this.trocaVeiculo?.filial)))
      .subscribe((filials: IFilial[]) => (this.filialsSharedCollection = filials));

    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.trocaVeiculo?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));
  }
}

import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { CompraVeiculoService } from '../service/compra-veiculo.service';
import { ICompraVeiculo } from '../compra-veiculo.model';
import { CompraVeiculoFormGroup, CompraVeiculoFormService } from './compra-veiculo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-compra-veiculo-update',
  templateUrl: './compra-veiculo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompraVeiculoUpdateComponent implements OnInit {
  isSaving = false;
  compraVeiculo: ICompraVeiculo | null = null;

  instituicaosSharedCollection: IInstituicao[] = [];
  veiculosSharedCollection: IVeiculo[] = [];
  filialsSharedCollection: IFilial[] = [];

  protected compraVeiculoService = inject(CompraVeiculoService);
  protected compraVeiculoFormService = inject(CompraVeiculoFormService);
  protected instituicaoService = inject(InstituicaoService);
  protected veiculoService = inject(VeiculoService);
  protected filialService = inject(FilialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompraVeiculoFormGroup = this.compraVeiculoFormService.createCompraVeiculoFormGroup();

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  compareVeiculo = (o1: IVeiculo | null, o2: IVeiculo | null): boolean => this.veiculoService.compareVeiculo(o1, o2);

  compareFilial = (o1: IFilial | null, o2: IFilial | null): boolean => this.filialService.compareFilial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compraVeiculo }) => {
      this.compraVeiculo = compraVeiculo;
      if (compraVeiculo) {
        this.updateForm(compraVeiculo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compraVeiculo = this.compraVeiculoFormService.getCompraVeiculo(this.editForm);
    if (compraVeiculo.id !== null) {
      this.subscribeToSaveResponse(this.compraVeiculoService.update(compraVeiculo));
    } else {
      this.subscribeToSaveResponse(this.compraVeiculoService.create(compraVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompraVeiculo>>): void {
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

  protected updateForm(compraVeiculo: ICompraVeiculo): void {
    this.compraVeiculo = compraVeiculo;
    this.compraVeiculoFormService.resetForm(this.editForm, compraVeiculo);

    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      compraVeiculo.instituicao,
    );
    this.veiculosSharedCollection = this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(
      this.veiculosSharedCollection,
      compraVeiculo.veiculo,
    );
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing<IFilial>(
      this.filialsSharedCollection,
      compraVeiculo.filial,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.compraVeiculo?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));

    this.veiculoService
      .query()
      .pipe(map((res: HttpResponse<IVeiculo[]>) => res.body ?? []))
      .pipe(
        map((veiculos: IVeiculo[]) => this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(veiculos, this.compraVeiculo?.veiculo)),
      )
      .subscribe((veiculos: IVeiculo[]) => (this.veiculosSharedCollection = veiculos));

    this.filialService
      .query()
      .pipe(map((res: HttpResponse<IFilial[]>) => res.body ?? []))
      .pipe(map((filials: IFilial[]) => this.filialService.addFilialToCollectionIfMissing<IFilial>(filials, this.compraVeiculo?.filial)))
      .subscribe((filials: IFilial[]) => (this.filialsSharedCollection = filials));
  }
}

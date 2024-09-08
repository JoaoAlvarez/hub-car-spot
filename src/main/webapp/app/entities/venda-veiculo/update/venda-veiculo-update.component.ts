import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVeiculo } from 'app/entities/veiculo/veiculo.model';
import { VeiculoService } from 'app/entities/veiculo/service/veiculo.service';
import { IInstituicao } from 'app/entities/instituicao/instituicao.model';
import { InstituicaoService } from 'app/entities/instituicao/service/instituicao.service';
import { IFilial } from 'app/entities/filial/filial.model';
import { FilialService } from 'app/entities/filial/service/filial.service';
import { IFinanceira } from 'app/entities/financeira/financeira.model';
import { FinanceiraService } from 'app/entities/financeira/service/financeira.service';
import { VendaVeiculoService } from '../service/venda-veiculo.service';
import { IVendaVeiculo } from '../venda-veiculo.model';
import { VendaVeiculoFormGroup, VendaVeiculoFormService } from './venda-veiculo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-venda-veiculo-update',
  templateUrl: './venda-veiculo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VendaVeiculoUpdateComponent implements OnInit {
  isSaving = false;
  vendaVeiculo: IVendaVeiculo | null = null;

  veiculosSharedCollection: IVeiculo[] = [];
  instituicaosSharedCollection: IInstituicao[] = [];
  filialsSharedCollection: IFilial[] = [];
  financeirasSharedCollection: IFinanceira[] = [];

  protected vendaVeiculoService = inject(VendaVeiculoService);
  protected vendaVeiculoFormService = inject(VendaVeiculoFormService);
  protected veiculoService = inject(VeiculoService);
  protected instituicaoService = inject(InstituicaoService);
  protected filialService = inject(FilialService);
  protected financeiraService = inject(FinanceiraService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VendaVeiculoFormGroup = this.vendaVeiculoFormService.createVendaVeiculoFormGroup();

  compareVeiculo = (o1: IVeiculo | null, o2: IVeiculo | null): boolean => this.veiculoService.compareVeiculo(o1, o2);

  compareInstituicao = (o1: IInstituicao | null, o2: IInstituicao | null): boolean => this.instituicaoService.compareInstituicao(o1, o2);

  compareFilial = (o1: IFilial | null, o2: IFilial | null): boolean => this.filialService.compareFilial(o1, o2);

  compareFinanceira = (o1: IFinanceira | null, o2: IFinanceira | null): boolean => this.financeiraService.compareFinanceira(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendaVeiculo }) => {
      this.vendaVeiculo = vendaVeiculo;
      if (vendaVeiculo) {
        this.updateForm(vendaVeiculo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendaVeiculo = this.vendaVeiculoFormService.getVendaVeiculo(this.editForm);
    if (vendaVeiculo.id !== null) {
      this.subscribeToSaveResponse(this.vendaVeiculoService.update(vendaVeiculo));
    } else {
      this.subscribeToSaveResponse(this.vendaVeiculoService.create(vendaVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendaVeiculo>>): void {
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

  protected updateForm(vendaVeiculo: IVendaVeiculo): void {
    this.vendaVeiculo = vendaVeiculo;
    this.vendaVeiculoFormService.resetForm(this.editForm, vendaVeiculo);

    this.veiculosSharedCollection = this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(
      this.veiculosSharedCollection,
      vendaVeiculo.veiculo,
    );
    this.instituicaosSharedCollection = this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(
      this.instituicaosSharedCollection,
      vendaVeiculo.instituicao,
    );
    this.filialsSharedCollection = this.filialService.addFilialToCollectionIfMissing<IFilial>(
      this.filialsSharedCollection,
      vendaVeiculo.filial,
    );
    this.financeirasSharedCollection = this.financeiraService.addFinanceiraToCollectionIfMissing<IFinanceira>(
      this.financeirasSharedCollection,
      vendaVeiculo.financeira,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.veiculoService
      .query()
      .pipe(map((res: HttpResponse<IVeiculo[]>) => res.body ?? []))
      .pipe(
        map((veiculos: IVeiculo[]) => this.veiculoService.addVeiculoToCollectionIfMissing<IVeiculo>(veiculos, this.vendaVeiculo?.veiculo)),
      )
      .subscribe((veiculos: IVeiculo[]) => (this.veiculosSharedCollection = veiculos));

    this.instituicaoService
      .query()
      .pipe(map((res: HttpResponse<IInstituicao[]>) => res.body ?? []))
      .pipe(
        map((instituicaos: IInstituicao[]) =>
          this.instituicaoService.addInstituicaoToCollectionIfMissing<IInstituicao>(instituicaos, this.vendaVeiculo?.instituicao),
        ),
      )
      .subscribe((instituicaos: IInstituicao[]) => (this.instituicaosSharedCollection = instituicaos));

    this.filialService
      .query()
      .pipe(map((res: HttpResponse<IFilial[]>) => res.body ?? []))
      .pipe(map((filials: IFilial[]) => this.filialService.addFilialToCollectionIfMissing<IFilial>(filials, this.vendaVeiculo?.filial)))
      .subscribe((filials: IFilial[]) => (this.filialsSharedCollection = filials));

    this.financeiraService
      .query()
      .pipe(map((res: HttpResponse<IFinanceira[]>) => res.body ?? []))
      .pipe(
        map((financeiras: IFinanceira[]) =>
          this.financeiraService.addFinanceiraToCollectionIfMissing<IFinanceira>(financeiras, this.vendaVeiculo?.financeira),
        ),
      )
      .subscribe((financeiras: IFinanceira[]) => (this.financeirasSharedCollection = financeiras));
  }
}

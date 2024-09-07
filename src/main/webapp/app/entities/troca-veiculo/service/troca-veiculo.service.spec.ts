import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITrocaVeiculo } from '../troca-veiculo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../troca-veiculo.test-samples';

import { RestTrocaVeiculo, TrocaVeiculoService } from './troca-veiculo.service';

const requireRestSample: RestTrocaVeiculo = {
  ...sampleWithRequiredData,
  dataTroca: sampleWithRequiredData.dataTroca?.format(DATE_FORMAT),
};

describe('TrocaVeiculo Service', () => {
  let service: TrocaVeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrocaVeiculo | ITrocaVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrocaVeiculoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TrocaVeiculo', () => {
      const trocaVeiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trocaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrocaVeiculo', () => {
      const trocaVeiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trocaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrocaVeiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrocaVeiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrocaVeiculo', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrocaVeiculoToCollectionIfMissing', () => {
      it('should add a TrocaVeiculo to an empty array', () => {
        const trocaVeiculo: ITrocaVeiculo = sampleWithRequiredData;
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing([], trocaVeiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trocaVeiculo);
      });

      it('should not add a TrocaVeiculo to an array that contains it', () => {
        const trocaVeiculo: ITrocaVeiculo = sampleWithRequiredData;
        const trocaVeiculoCollection: ITrocaVeiculo[] = [
          {
            ...trocaVeiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing(trocaVeiculoCollection, trocaVeiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrocaVeiculo to an array that doesn't contain it", () => {
        const trocaVeiculo: ITrocaVeiculo = sampleWithRequiredData;
        const trocaVeiculoCollection: ITrocaVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing(trocaVeiculoCollection, trocaVeiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trocaVeiculo);
      });

      it('should add only unique TrocaVeiculo to an array', () => {
        const trocaVeiculoArray: ITrocaVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trocaVeiculoCollection: ITrocaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing(trocaVeiculoCollection, ...trocaVeiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trocaVeiculo: ITrocaVeiculo = sampleWithRequiredData;
        const trocaVeiculo2: ITrocaVeiculo = sampleWithPartialData;
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing([], trocaVeiculo, trocaVeiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trocaVeiculo);
        expect(expectedResult).toContain(trocaVeiculo2);
      });

      it('should accept null and undefined values', () => {
        const trocaVeiculo: ITrocaVeiculo = sampleWithRequiredData;
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing([], null, trocaVeiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trocaVeiculo);
      });

      it('should return initial array if no TrocaVeiculo is added', () => {
        const trocaVeiculoCollection: ITrocaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addTrocaVeiculoToCollectionIfMissing(trocaVeiculoCollection, undefined, null);
        expect(expectedResult).toEqual(trocaVeiculoCollection);
      });
    });

    describe('compareTrocaVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrocaVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareTrocaVeiculo(entity1, entity2);
        const compareResult2 = service.compareTrocaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareTrocaVeiculo(entity1, entity2);
        const compareResult2 = service.compareTrocaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareTrocaVeiculo(entity1, entity2);
        const compareResult2 = service.compareTrocaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

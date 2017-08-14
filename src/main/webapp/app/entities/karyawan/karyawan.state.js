(function() {
    'use strict';

    angular
        .module('myCompanyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('karyawan', {
            parent: 'entity',
            url: '/karyawan?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myCompanyApp.karyawan.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/karyawan/karyawans.html',
                    controller: 'KaryawanController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('karyawan');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('karyawan-detail', {
            parent: 'karyawan',
            url: '/karyawan/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myCompanyApp.karyawan.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/karyawan/karyawan-detail.html',
                    controller: 'KaryawanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('karyawan');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Karyawan', function($stateParams, Karyawan) {
                    return Karyawan.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'karyawan',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('karyawan-detail.edit', {
            parent: 'karyawan-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/karyawan/karyawan-dialog.html',
                    controller: 'KaryawanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Karyawan', function(Karyawan) {
                            //return Karyawan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('karyawan.new', {
            parent: 'karyawan',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/karyawan/karyawan-dialog.html',
                    controller: 'KaryawanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nama: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('karyawan', null, { reload: 'karyawan' });
                }, function() {
                    $state.go('karyawan');
                });
            }]
        })
        .state('karyawan.edit', {
            parent: 'karyawan',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/karyawan/karyawan-dialog.html',
                    controller: 'KaryawanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Karyawan', function(Karyawan) {
                            return Karyawan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('karyawan', null, { reload: 'karyawan' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('karyawan.shows', {
            parent: 'karyawan',
            url : '/{id}/shows',
            views: {
                'content@': {
                    templateUrl: 'app/entities/karyawan/karyawanShow.html',
                    controller: 'KaryawanController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('karyawan.delete', {
            parent: 'karyawan',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/karyawan/karyawan-delete-dialog.html',
                    controller: 'KaryawanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Karyawan', function(Karyawan) {
                            return Karyawan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('karyawan', null, { reload: 'karyawan' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
